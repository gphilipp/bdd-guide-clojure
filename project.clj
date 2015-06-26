(defproject
  calculator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC2"]
                 [info.cukes/cucumber-clojure "1.2.2"]]
  :plugins [[lein-cucumber "1.0.2"]]
  :test-paths ["test/acceptance/features" "test/acceptance/step_definitions" "test/clj"]
  :cucumber-feature-paths ["test/acceptance/features"]
  )
