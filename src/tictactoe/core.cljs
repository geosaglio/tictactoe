(ns tictactoe.core
  (:require [reagent.core :refer [render]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

; (require '[tictactoe.core :refer [state]])
(defonce state (atom {:title "Geo's game of TicTacToe"
                      }))
; Lire : @state
; Ecrire : (swap! state assoc :text "Hello Geo")

(defn banner
  [state]
  [:h1 (:title @state)])

(defn body
  [state]
  [:div.ttleslignes
   [:div.ligne1
    [:button.button1 {:type "button"}
     "Place here"]
    [:button.button2 {:type "button"}
     "Place here"]
    [:button.button3 {:type "button"}
     "Place here"]]
   [:div.ligne2
    [:button.button2-1 {:type "button"}
     "Place here"]
    [:button.button2-2 {:type "button"}
     "Place here"]
    [:button.button2-3 {:type "button"}
     "Place here"]]
   [:div.ligne3
    [:button.button3-1 {:type "button"}
     "Place here"] 
    [:button.button3-2 {:type "button"}
     "Place here"]
    [:button.button3-3 {:type "button"}
     "Place here"]]])


(defn my-content
  [state]
  [:div
   [banner state]
   [body state]])

(defn my-app
  []
  [my-content state])
(render my-app
        (.getElementById js/document "app"))
(add-watch state :rerender #(render my-app
                                    (.getElementById js/document "app")))