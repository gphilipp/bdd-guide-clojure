(ns calculator.core-test
  (:require [clojure.test :refer [deftest]])
  (:import (cucumber.runtime RuntimeOptions)
           (cucumber.runtime.io MultiLoader)))

#_(deftest run-cukes
  (. cucumber.api.cli.Main (main
                             (into-array ["--format"
                                          "pretty"
                                          "--glue"
                                          "test/acceptance/step_definitions"
                                          "test/acceptance/features"
                                          ]))))


(deftest run-cukes-no-exit
  (let [classloader (.getContextClassLoader (Thread/currentThread))
        runtime-options (RuntimeOptions.
                          (System/getProperties)
                          (into-array ["--format"
                                       "pretty"
                                       "--glue"
                                       "test/acceptance/step_definitions"
                                       "test/acceptance/features"
                                       ]))
        runtime (cucumber.runtime.Runtime. (MultiLoader. classloader) classloader runtime-options)]
    (doto runtime
      (.writeStepdefsJson)
      (.run))
    ))
