# BDD in a Clojure project with cucumber-jvm, Leiningen and Cursive

First of all, because we never say "thank you" enough, kudos to:
 
- Aslak Hellesøy for creating [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) which is a very useful tool to write much better software.
- Colin Fleming for [Cursive](https://cursiveclojure.com) which rocks
- Phil Hagelberg for [Leiningen](http://leiningen.org) which does so much for you.
- Rich Hickey for [Clojure](http://clojure.org) for which I have no words strong enough to praise.


I'll walk you through a very simple example to demonstrate how you can do BDD in Clojure with the tools above. 

## Step 1 - Launch BDD tests with Leiningen

First, create a fresh new project:

``` bash
lein new calculator
``` 

Create a new folder called `features` and add a new file called `addition.clj` in that directory with the following content: 

``` gherkin
Feature: Addition
  In order to avoid silly mistakes
  As a math idiot
  I want to be told the sum of two numbers

  Scenario: Add two numbers
    Given I have entered 50 into the calculator
    And I have entered 70 into the calculator
    When I press add
    Then the result should be 120 on the screen
```

Now, let's implement this scenario. We have to create the glue (called step definitions) which will link the
BDD text to our future code. Add a folder under `features` called `step_definitions` and create a file 
named `addition.clj` in the `features/step_definitions` folder: 

``` clojure
(use 'calculator.core) ;; yes, no namespace declaration
(use 'clojure.test)

(def world (atom {:inputs []
                  :result nil}))

(Given #"^I have entered (\d+) into the calculator$" [input]
       (swap! world update :inputs conj (bigdec input))
       )

(When #"^I press add$" []
      (swap! world assoc :actual-result (reduce + (:inputs @world))))


(Then #"^the result should be (\d+) on the screen$" [result]
      (is (= (bigdec result) (:actual-result @world))))
```
 

It's time to implement the complex mechanic of our calculator. Create the following namespace in the `src` directory:

``` clojure
(ns calculator.core)

(defn addition
  "Adds numbers"
  [x y]
  (+ x y))
```

Update your `project.clj` and add the [lein-cucumber](https://github.com/nilswloka/lein-cucumber) plugin:

``` clojure
:plugins [[lein-cucumber "1.0.2"]]
```

You can now open a command prompt and type `lein cucumber`, it should produce the following:

```
Running cucumber...
Looking for features in:  [features]
Looking for glue in:  [features/step_definitions]
....
```

The 4 points above indicate that the test are passing. Well done!


Note: The BDD example was taken from the official [Cucumber website](http://cukes.info).


## Step 2 - Launch BDD tests with Cursive

At that point, we'd like to be able to launch the tests from Cursive which uses `clojure.test`.
The cucumber-jvm repo has an [example](https://github.com/cucumber/cucumber-jvm/blob/master/examples/clojure_cukes/test/clojure_cukes/test/core.clj).
Let's define something similar in the `test` folder:

``` clojure
(ns calculator.core-test
  (:require [clojure.test :refer [deftest]])
  
(deftest run-cukes
  (. cucumber.api.cli.Main (main
                             (into-array ["--format"
                                          "pretty"
                                          "--glue"
                                          "feature/step_definitions"]))))
```

If you execute the "Run tests in current NS in REPL" command within the `calculator.core-test` ns, 
you should get the following exception:
          
```
Exception java.lang.ClassNotFoundException: cucumber.api.cli.Main, compiling:(calculator/test/calculator/core_test.clj:5:3)
```



First, let's modify the test directory structure. 
```step_definitions```needs to be a test root so that `addition.clj` is found when executing the tests.


lein cucumber --glue test/acceptance/step_definitions


## Step 3 - Launch BDD tests with IntelliJ cucumber-jvm plugin
 
 You can right click in the `addition.feature` file on the `Feature:` word or on a `Scenario:` word and click "Create Feature:..." or "Create Scenario:..."
 