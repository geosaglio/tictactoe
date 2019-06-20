(ns tictactoe.core
  (:require [reagent.core :as r :refer [render]]))

(enable-console-print!)

(comment
 (require '[tictactoe.core :refer [state]]))

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

(defn click-button
  [state button-clicked]
  (let [symbols {:joueur-1 :circle
                 :joueur-2 :cross}
        player (:turn @state)]
    (swap! state update :board assoc button-clicked (player symbols)))
  (toggle-player-turn state))

(defn ttt-button
  [state localization css]
  [:button {:type "button"
            :class css
            :on-click #(click-button state localization)}
   "Place here"])

(defn body
  [state]
  [:div.ttleslignes
   [:div.ligne1
    [ttt-button state :haut-gauche "button1"]
    [ttt-button state :haut-milieu "button2"]
    [ttt-button state :haut-droite "button3"]]

   [:div.ligne2
    [ttt-button state :milieu-gauche "button2-1"]
    [ttt-button state :milieu-milieu "button2-2"]
    [ttt-button state :milieu-droite "button2-3"]]

   [:div.ligne3
    [ttt-button state :bas-gauche "button3-1"]
    [ttt-button state :bas-milieu "button3-2"]
    [ttt-button state :bas-droite "button3-3"]]])

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