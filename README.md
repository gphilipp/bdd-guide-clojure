# BDD in a Clojure project with cucumber-jvm, Leiningen and Cursive

First of all, because we never say "thank you" enough, kudos to:
 
- Rich Hickey for [Clojure](http://clojure.org) for which I have no words strong enough to praise.
- Colin Fleming for [Cursive](https://cursiveclojure.com) which rocks
- Aslak Hellesøy for [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) which is a very useful tool to write much better software.
- Phil Hagelberg for [Leiningen](http://leiningen.org) which does so much for you.
- Nils Wloka for the [leiningen-cucumber](https://github.com/nilswloka/lein-cucumber) plugin which helped me getting started. 

I'll walk you through a very simple example to demonstrate how you can do BDD in Clojure with the tools above.
The main objective of this repo is to have a minimalistic example for Colin to improve support for cucumber-jvm 
in Cursive (Colin, check out the end of this README for the wishlist !). 

I'll walk you through a very simple example to demonstrate how you can do BDD in Clojure with the tools above.
The main objective of this repo is to have a minimalistic example for Colin to improve support for cucumber-jvm 
in Cursive (Colin, check out the end of this README for the wishlist !). 

## Step 1 - Install plugins

Install the "Gherkin" and "Cucumber for Java" plugins from Jetbrains plugin repository.
Detailed instructions here: https://www.jetbrains.com/idea/help/cucumber.html

## Step 2 - Launch BDD tests with Leiningen

First, create a fresh new project:

``` bash
lein new calculator
``` 

Create a `features` folder and add a new file named `addition.clj` in that folder with the following content: 

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

Since you've installed the Gherkin plugin you should have syntax highlighting and a nice little Cucumber icon for the feature file.

Now, let's implement this scenario. We have to create the glue (called step definitions) which will link the
BDD text to our future code. Add a folder under `features` called `step_definitions` and create a file 
named `addition.clj` in the `features/step_definitions` folder: 

``` clojure
(use 'calculator.core) ;; yes, no namespace declaration
(use 'clojure.test)

(def world (atom {:inputs []
                  :actual-result nil}))

(Given #"^I have entered (\d+) into the calculator$" [input]
       (swap! world update :inputs conj (bigdec input)))

(When #"^I press add$" []
      (swap! world assoc :actual-result (reduce add (:inputs @world))))

(Then #"^the result should be (\d+) on the screen$" [result]
      (is (= (bigdec result) (:actual-result @world))))
```
 
It's time to implement the complex mechanic of our calculator. Create the following namespace in the `src` directory:

``` clojure
(ns calculator.core)

(def add
  "Adds numbers"
  +)
```

Update your `project.clj` and add the [lein-cucumber](https://github.com/nilswloka/lein-cucumber) plugin:

``` clojure
:plugins [[lein-cucumber "1.0.2"]]
```

Then add a clojure.test test as instructed on the [cucumber-jvm clojure how-to](https://github.com/cucumber/cucumber-jvm/tree/master/clojure) :

``` clojure
(ns calculator.core-test
  (:require [clojure.test :refer [deftest]])
  
(deftest run-cukes
  (. cucumber.api.cli.Main (main
                             (into-array ["--format"
                                          "pretty"
                                          "--glue"
                                          "features/step_definitions"]))))
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


## Step 2 - Edit the glue file with Cursive

At that point, `addition.clj` looks like this in Cursive: 


If you execute the "Run tests in current NS in REPL" command within the `calculator.core-test` ns, 
you should get the following exception:

![](images/cursive-support-1.png)


## Step 3 - Launch BDD tests with Cursive

It'd be better if we could launch the tests from Cursive with "Run tests in current NS in REPL" which supports only `clojure.test` for now.
If you run the command within the `calculator.core-test` ns, you should get the following exception:
          
```
Exception java.lang.ClassNotFoundException: cucumber.api.cli.Main, compiling:(calculator/test/calculator/core_test.clj:5:3)
```

That's normal because the cucumber.api.cli.Main is located in the cucumber-clojure jar which is not in your
  leiningen dependencies. It worked with the lein-cucumber plugin above because it [adds dynamically](https://github.com/nilswloka/lein-cucumber/blob/55c24a7a5bfee070bd45cef68732beb1c36b8ce7/src/leiningen/cucumber.clj#L36-L37) 
  the dependency when you run the `lein cucumber` command.

So, let's add the same version (to avoid conflicts) explicitly in our `:dependencies` vector:

```
[info.cukes/cucumber-clojure "1.1.1"]
```

First, let's modify the test directory structure. 
```step_definitions```needs to be a test root so that `addition.clj` is found when executing the tests.

``` bash
lein cucumber --glue test/acceptance/step_definitions
```

...

Update those keys in  `project.clj`

```clojure
:test-paths ["test/acceptance/features" "test/acceptance/step_definitions" "test/clj"]
:cucumber-feature-paths ["test/acceptance/features"]
```

## Step 4 - Launch BDD tests with IntelliJ cucumber-jvm plugin
 
 You can right click in the `addition.feature` file on the `Feature:` word or on a `Scenario:` word and click "Create Feature:..." or "Create Scenario:..."
 
 
# Cursive BDD wishlist

I'd like Cursive or Jetbrains to create a "Cucumber for Clojure" plugin which would support the same as the "Cucumber for Java" plugin :
- Autocompletion for steps.
- "find usage" from stepdefs to steps.
- Right click on a scenario or feature section in a gherkin file and run it or debug it
 
 Hey, Scala has a plugin: https://plugins.jetbrains.com/plugin/7460?pr=mps


 