(ns achesnais.trousseau)

(defn map-vals [f m]
  (reduce-kv (fn [m k v] (assoc m k (f v))) {} m))

(defn map-keys [f m]
  (reduce-kv (fn [m k v] (assoc m (f k) v)) {} m))
