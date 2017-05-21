(ns recepti.models.db
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as str]
            [korma.core :refer :all]
            [korma.db :refer :all]
            )
  (:import java.sql.DriverManager))


(defdb db (mysql {:db "recepti"
                     :user "admin"
                     :password "admin"}))
(defentity user
  (table :korisnici))

(defentity recept
  (table :recepti))

(defn vrati-korisnika [username password]
  (select user
  (where {:username username :password password})))

(defn dodaj-korisnika [params]
  (insert user
  (values params)))

(defn vrati-recepte []
  (select recept
  (order :napisano :DESC)))

(defn dodaj-recept [params]
  (insert recept
  (values params)))
