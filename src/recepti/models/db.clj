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

;(def dbs {:classname "com.mysql.jdbc.Driver"
 ;        :subprotocol "mysql"
  ;       :subname "//localhost:3306/recepti"
   ;      :user "admin"
    ;     :password "admin"})

(defentity user
  (table :korisnici))

(defentity admin
  (table :admini))

(defentity recept
  (table :recepti))

(defentity utisak
  (table :utisak))

(defentity lajk
  (table :lajkovi))

(defentity komentar
  (table :komentarinarecepte))

(defn vrati-korisnika [username password]
  (select user
  (where {:username username :password password})))

(defn lajkovi-za-recept [id]
  (select lajk
  (where {:recept id})))

(defn komentari-za-recept [id]
  (select komentar
  (where {:recept id})))

(defn vrati-admina [username password]
  (select admin
  (where {:username username :password password})))


(defn ovaj-user-lajkovao [id user]
  (select lajk
  (where {:user user :recept id })))

(defn dodaj-lajk-na-recept [receptid ostavio]
  (println receptid ostavio)
  (insert lajk
  (values {:user ostavio
           :recept receptid
          })))

(defn dodaj-komentar-na-recept [tekst ostavio datum receptid]
  (println receptid ostavio)
  (insert komentar
  (values {
            :tekst tekst
            :ostavio ostavio
            :datum datum
             :recept receptid
          })))

(defn dodaj-korisnika [ime prezime email username password]
  (insert user
  (values {:ime ime
           :prezime prezime
           :email email
           :username username
           :password password
            })))

(defn vrati-recepte []
  (select recept (where {:dozvoljeno true}) (order :napisano :DESC)))

(defn vrati-zabranjene-recepte[]
  (select recept (where {:dozvoljeno false}) (order :napisano :DESC)))

(defn vrati-utiske []
  (select utisak
  (order :datum :DESC)))

(defn vrati-sve-recepte []
  (select recept
  (order :napisano :DESC)))

(defn vrati-poslednji-utisak []
  (select count utisak))

(defn dodaj-recept [naziv sastojci opis slika napisano receptod dozvoljeno]
  (insert recept
  (values {:naziv naziv
           :sastojci sastojci
           :opis opis
           :slika slika
           :napisano napisano
           :receptod receptod
           :dozvoljeno dozvoljeno
            })))

(defn izmeni-utisak [id naziv opis]
  (update utisak
  (set-fields {:naziv naziv
               :opis opis
               })
  (where {:idUtiska id})))

(defn dodaj-utisak [naziv opis ostavio datum]
  (insert utisak
  (values {:naziv naziv
           :opis opis
           :ostavio ostavio
           :datum datum
            })))

(defn obrisi-utisak [id]
  (delete utisak
  (where {:idUtiska id})))

(defn obrisi-lajk-sa-recepta [id]
  (delete lajk
  (where {:id id})))

(defn obrisi-recept [id]
  (delete recept
  (where {:receptID id})))

(defn dozvoli-recept [id]
  (update recept
  (set-fields {:dozvoljeno true})
  (where {:receptID id})))

(defn zabrani-recept [id]
  (update recept
  (set-fields {:dozvoljeno false})
  (where {:receptID id})))

(defn vrati-utisak [id]
  (select utisak
  (where {:idUtiska id})))

(defn vrati-recept-id [id]
  (select recept
  (where {:receptID id})))

(defn get-text [text]
  (str "%" text "%"))

(defn nadji-recepte-po-kriterijumu [text]
  (select recept
            (where
              (or (like :naziv (get-text text))
                  (like :sastojci (get-text text))
                  (like :opis (get-text text))))
            (order :naziv :ASC)))
