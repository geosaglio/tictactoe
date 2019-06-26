(ns tictactoe.core
  (:require [reagent.core :as r :refer [render]]))

(enable-console-print!)

(comment
 (require '[tictactoe.core :refer [state]]))

;; define your app data so that it doesn't get over-written on reload

; (require '[tictactoe.core :refer [state]])
(def initial-state {:board {}
                    :turn :joueur-1
                    })

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
      (.setTimeout js/window #(.alert js/window (str player " a gagne")) 16)
      (swap! state assoc :winner player))))

(defn click-button
  [state button-clicked]
  (if (nil? (:winner @state))
    (do
      (let [player (:turn @state)]
        (swap! state update :board assoc button-clicked (player symbols)))
      (victoire state)
      (toggle-player-turn state))))

(defn ttt-button
  [state localization css]
  (cond 
    (= (get-in @state [:board localization]) :cross) 
    [:svg {:view-box "0 0 100 100"}
     [:line {:x1 "10" :y1 "100" :x2 "100" :y2 "10"
             :stroke "black" :stroke-width "10"}]
     [:line {:x1 "10" :y1 "10" :x2 "100" :y2 "100"
             :stroke "black" :stroke-width "10"}]]
    (= (get-in @state [:board localization]) :circle)
    [:svg {:view-box "0 0 100 100"} 
     [:circle {:cx "50" :cy "50" :r "45"
               :stroke "black" :stroke-width "10" :fill "white"}]]
    (nil? (get-in @state [:board localization]))
    [:svg {:view-box "0 0 100 100"}
     [:rect {:width 100 :height 100 
             :fill "white" :stroke "black" :stroke-width "10"
             :on-click #(click-button state localization)}]]))

(defn body
  [state]
  [:div.totale [:div.grid-container
                [:div.grid-item [ttt-button state :haut-gauche "button1"]]
                [:div.grid-item [ttt-button state :haut-milieu "button2"]]
                [:div.grid-item [ttt-button state :haut-droite "button3"]]
                [:div.grid-item [ttt-button state :milieu-gauche "button2-1"]]
                [:div.grid-item [ttt-button state :milieu-milieu "button2-2"]]
                [:div.grid-item [ttt-button state :milieu-droite "button2-3"]]
                [:div.grid-item [ttt-button state :bas-gauche "button3-1"]]
                [:div.grid-item [ttt-button state :bas-milieu "button3-2"]]
                [:div.grid-item [ttt-button state :bas-droite "button3-3"]]]
   
   [:button {:type "button"
             :class "restartbutton"
             :on-click #(reset! state initial-state)}"Restart"]])

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
