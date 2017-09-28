(ns trousseau.time)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Coercions

(defprotocol ITimeCoercions
  (date-time [x zone]
    "Create a java.time.ZonedDateTime object from `x` and the provided time zone specification. `x` can be any of java.util.Date (easily generated via the `#inst` reader literal), a java.sql.Timestamp or another java.time.ZonedDateTime.
  If providing a ZonedDateTime, `date-time` will return a new ZonedDateTime of the same instant but at the new time zone.
  `zone` must be a valid time zone code, e.g. \"Europe/London\" or \"UTC+2\"")
  (sql-timestamp [x])
  (inst [x]))

(extend-protocol ITimeCoercions

  java.util.Date
  (date-time [x zone] (java.time.ZonedDateTime/ofInstant (.toInstant x) (java.time.ZoneId/of zone)))
  (sql-timestamp [x] (java.sql.Timestamp. (.getTime x)))
  (inst [x] x)

  java.time.ZonedDateTime
  (date-time [x zone] (.withZoneSameInstant x (java.time.ZoneId/of zone)))
  (sql-timestamp [x] (java.sql.Timestamp. (.toEpochMilli (.toInstant x))))
  (inst [x] (java.util.Date/from (.toInstant x)))

  java.sql.Timestamp
  (date-time [x zone] (java.time.ZonedDateTime/ofInstant (.getTime x) (java.time.ZoneId/of zone)))
  (sql-timestamp [x] x)
  (inst [x] (java.util.Date. (.getTime x))))

(defn utc-date-time [x]
  (date-time x "UTC"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Temporal amounts

(defn seconds [n]
  (java.time.Duration/ofSeconds n))

(defn minutes [n]
  (java.time.Duration/ofMinutes n))

(defn hours [n]
  (java.time.Duration/ofHours n))

(defn days [n]
  (java.time.Period/ofDays n))

(defn months [n]
  (java.time.Period/ofMonths n))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Operations

(defn before? [^java.time.ZonedDateTime a
               ^java.time.ZonedDateTime b]
  (.isBefore a b))

(defn after? [^java.time.ZonedDateTime a
              ^java.time.ZonedDateTime b]
  (.isAfter a b))

(defn plus [^java.time.ZonedDateTime zoned-date-time
            ^java.time.temporal.TemporalAmount temporal-amount]
  (.plus zoned-date-time temporal-amount))

(defn minus [^java.time.ZonedDateTime zoned-date-time
             ^java.time.temporal.TemporalAmount temporal-amount]
  (.minus zoned-date-time temporal-amount))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;

(defn periodic-seq
  ([start temporal-amount]
   (iterate #(plus % temporal-amount) start))
  ([start end temporal-amount]
   (take-while #(before? % end) (periodic-seq start temporal-amount))))
