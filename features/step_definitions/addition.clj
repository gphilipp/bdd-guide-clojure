(use 'calculator.core)
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
