(ns tictactoe.core
  (:require [reagent.core :as r :refer [render]]))

(enable-console-print!)

(comment
 (require '[tictactoe.core :refer [state]]))

;; define your app data so that it doesn't get over-written on reload

; (require '[tictactoe.core :refer [state]])
(def initial-state {:board {:haut-gauche nil
                            :haut-milieu nil
                            :haut-droite nil
                            :milieu-gauche nil
                            :milieu-milieu nil
                            :milieu-droite nil
                            :bas-gauche nil
                            :bas-milieu nil
                            :bas-droite nil}
                    :turn :joueur-1})

(defonce state (r/atom initial-state))
; Lire : @state
; Ecrire : (swap! state assoc :text "Hello Geo")

; (reset! state initial-state))

(def ^:const symbols {:joueur-1 :circle
                      :joueur-2 :cross})

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

(defn victoire
  [state]
  (let [board (:board @state)
        player (:turn @state)]
    (when (or (= (player symbols) (:haut-gauche board) (:haut-milieu board) (:haut-droite board))
              (= (player symbols) (:haut-gauche board) (:milieu-milieu board) (:bas-droite board))
              (= (player symbols) (:haut-gauche board) (:milieu-gauche board) (:bas-gauche board))
              (= (player symbols) (:milieu-gauche board) (:milieu-milieu board) (:milieu-droite board))
              (= (player symbols) (:bas-gauche board) (:bas-milieu board) (:bas-droite board))
              (= (player symbols) (:haut-milieu board) (:milieu-milieu board) (:bas-milieu board))
              (= (player symbols) (:haut-droite board) (:milieu-droite board) (:bas-droite board))
              (= (player symbols) (:bas-gauche board) (:milieu-milieu board) (:haut-droite board)))
      (.alert js/window (str player " a gagne")))))

(defn click-button
  [state button-clicked]
  (let [player (:turn @state)]
    (swap! state update :board assoc button-clicked (player symbols)))
  (victoire state)
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
    [ttt-button state :bas-droite "button3-3"]]
   
   [:button {:type "button"
             :class "restartbutton"
             :on-click #(reset! state initial-state)}
    "Restart"]])

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