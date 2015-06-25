(ns calculator.core-test
  (:require [clojure.test :refer [deftest]]))

(deftest run-cukes
  (. cucumber.api.cli.Main (main (into-array ["--plugin" "pretty" "--glue"]))))

