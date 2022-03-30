(ns math.core
  (:require
   [reagent.core :as rc]
   [reagent.dom :as rd]
   [reitit.frontend :as rtf]
   [reitit.frontend.easy :as rtfe]
   [reitit.coercion.spec :as rtcs]
   [spec-tools.data-spec :as stds]
   #_[fipp.edn :as fe]
   [garden.core :as gdc]))

(def style
  (str
   (gdc/css [:body {:font-family :Helvetica
                    :line-height :1.5}])
   (gdc/css [:ul {:list-style-type :none
                  :margin 0
                  :padding 0
                  :overflow "hidden"
                  :background-color "hsl(171, 100%, 41%)"}])
   (gdc/css [:li {:float :left}])
   (gdc/css [:li :a {:display :block
                     :color :white
                     :text-align :center
                     :padding "6px 8px"
                     :text-decoration :none}])
   (gdc/css [:.container {:margin :auto
                          :width :840px}])
   (gdc/css [:.bordered {:border "1px solid #000"}])))

(defn about-page []
  [:div {:class "container"}
   [:h1 "Welcome to Darren's Linear Algrabra Study"]
   [:h3 "Example 7"]
   [:p (str "What vector \\(\\begin{bmatrix} x \\\\ y \\end{bmatrix}\\) satisfies "
            "\\(\\begin{bmatrix} 2 \\\\ 4 \\end{bmatrix}\\)")]])

(defn item-page [match]
  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    [:div
     [:h2 "Selected item " id]
     (when (:foo query)
       [:p "Optional foo query param: " (:foo query)])]))

(defonce match (rc/atom nil))

(defn frame []
  [:div
   [:style style]
   [:ul
    [:li [:a {:href (rtfe/href ::about)} "About"]]
    [:li [:a {:href (rtfe/href ::item {:id 1})} "01"]]
    [:li [:a {:href (rtfe/href ::item {:id 2})} "02"]]]
   (when @match
     (let [view (:view (:data @match))]
       [view @match]))])

(def routes
  [["/"
    {:name ::about
     :view about-page}]
   #_["/about"
      {:name ::about
       :view about-page}]
   ["/item/:id"
    {:name ::item
     :view item-page
     :parameters {:path {:id int?}
                  :query {(stds/opt :foo) keyword?}}}]])

(defn render! []
  (rd/render
   [frame]
   (.getElementById js/document "root")))

(defn ^:export init! []
  (rtfe/start!
   (rtf/router routes {:data {:coercion rtcs/coercion}})
   (fn [m] (reset! match m))
   {:use-fragment true})
  (render!))

(defn ^:export refresh! []
  (render!))
