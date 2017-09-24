(ns trousseau.core)

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

(defn rsort-by [keyfn coll]
  (sort-by keyfn reverse-compare coll))

(defn inject
  ([] (inject {}))
  ([{:keys [dry-run? verbose?
            include exclude]
     :or   {verbose? false
            dry-run? false
            include  #{}
            exclude  #{}}}]
   (let [include (set (map symbol include))
         exclude (set (map symbol exclude))]
     (doseq [[sym f] (cond->> (ns-publics 'trousseau.core)

                       (not (empty? include))
                       (filter #(contains? include (key %)))

                       (not (empty? exclude))
                       (remove #(contains? exclude (key %))))]
       (when verbose?
         (println ::adding f :to *ns* :as sym))
       (when-not dry-run?
         (intern *ns* sym f))))))
