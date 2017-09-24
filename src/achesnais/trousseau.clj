(ns achesnais.trousseau)

(defn map-vals [f m]
  (reduce-kv (fn [m k v] (assoc m k (f v))) {} m))

(defn map-keys [f m]
  (reduce-kv (fn [m k v] (assoc m (f k) v)) {} m))

(defn proportions [coll]
  (let [n (count coll)]
    (map-vals #(float (/ % n)) (frequencies coll))))

(defn- reverse-compare [x y]
  (compare y x))

(defn rsort [coll]
  (sort reverse-compare coll))

(def rsort-by [keyfn coll]
  (sort-by keyfn reverse-compare coll))
