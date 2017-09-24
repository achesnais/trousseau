(ns trousseau.time)

(defprotocol ITimeCoercions
  (utc-zoned-date-time [x])
  (sql-timestamp [x])
  (inst [x]))

(extend-protocol ITimeCoercions

  java.util.Date
  (utc-zoned-date-time [x] (java.time.ZonedDateTime/ofInstant (.toInstant x) (java.time.ZoneId/of "UTC")))
  (sql-timestamp [x] (java.sql.Timestamp. (.getTime x)))
  (inst [x] x)

  java.time.ZonedDateTime
  (zoned-date-time [x] x)
  (sql-timestamp [x] (java.sql.Timestamp. (.toEpochMilli (.toInstant x))))
  (inst [x] (java.util.Date/from (.toInstant x)))

  java.sql.Timestamp
  (utc-zoned-date-time [x] (java.time.ZonedDateTime/ofInstant (.toInstant x) (java.time.ZoneId/of "UTC")))
  (sql-timestamp [x] x)
  (inst [x] (java.util.Date. (.getTime x))))
