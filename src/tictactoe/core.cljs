(ns tictactoe.core
  (:require [reagent.core :as r :refer [render]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

; (require '[tictactoe.core :refer [state]])
(defonce state (r/atom {:board {:haut-gauche nil
                                :haut-milieu nil
                                :haut-droite nil
                                :milieu-gauche nil
                                :milieu-milieu nil
                                :milieu-droite nil
                                :bas-gauche nil
                                :bas-milieu nil
                                :bas-droite nil}
                      :turn :joueur-1}))
; Lire : @state
; Ecrire : (swap! state assoc :text "Hello Geo")

(defn banner
  [state]
  (let [m @state]
    [:h1 (str "Geo's game of TicTacToe - " 
              (:turn m))]))

(defn who's-turn?
  [state]
  (:turn @state))

(defn change-turn
  [state player]
  (swap! state assoc :turn player))

(defn toggle-player-turn
  [state]
  (let [current-player (:turn @state)]
    (if (= :joueur-1 current-player)      
      (change-turn state :joueur-2)
      (change-turn state :joueur-1))))

(defn click-button-1
  [state]
  (toggle-player-turn state))

(defn body
  [state]
  [:div.ttleslignes
   [:div.ligne1
    [:button.button1 {:type "button"
                      :on-click (fn [event]
                                  (click-button-1 state))}
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
(add-watch state 
           :rerender 
           #(render my-app
                    (.getElementById js/document "app")))