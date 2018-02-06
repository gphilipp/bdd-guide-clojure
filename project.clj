(defproject
  calculator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [info.cukes/cucumber-clojure "1.2.5" :scope "test"]
                 [net.masterthought/cucumber-reporting "3.14.0" :scope "test"]]
  :plugins [[org.clojars.punkisdead/lein-cucumber "1.0.7"]]
  :test-paths ["test/acceptance/features" "test/acceptance/step_definitions" "test/clj"
               "scripts"]
  :cucumber-feature-paths ["test/acceptance/features"]

  )
