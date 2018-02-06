(ns calculator.runcukes
  (:require [clojure.test :refer [deftest is]])
  (:import (cucumber.runtime RuntimeOptions)
           (cucumber.runtime.io MultiLoader)))


(deftest run-cukes
  (. cucumber.api.cli.Main
     (main (into-array ["--plugin"
                        "pretty"
                        "--glue"
                        "test/acceptance/step_definitions"
                        "test/acceptance/features"]))))

