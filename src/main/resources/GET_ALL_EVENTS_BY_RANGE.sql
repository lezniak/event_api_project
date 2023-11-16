DELIMITER //
CREATE PROCEDURE GET_EVENTS_BY_RANGE(IN user_lat decimal(10,6), IN user_lng decimal(10,6), IN user_range decimal(10,6))
BEGIN
SELECT e.*, ea.*, 111.045 * DEGREES(ACOS(LEAST(1.0, COS(RADIANS(latpoint))
    * COS(RADIANS(ea.lat))
    *COS(RADIANS(longpoint) - RADIANS(RADIANS(ea.lng))
        +SIN(RADIANS(latpoint))
             *SIN(RADIANS(ea.lat)))))
    )
    AS distance_in_km FROM event e INNER JOIN event_address ea ON e.address_id = ea.id
                                   JOIN (
    SELECT user_lat AS latpoint, user_lng AS longpoint,
           user_range AS radius, 111.045 AS distance_unit
) AS p ON 1=1
WHERE ea.lat
    BETWEEN p.latpoint - (p.radius / p.distance_unit)
    AND p.latpoint + (p.radius / p.distance_unit)
  AND ea.lng
    BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))
    AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))
ORDER BY distance_in_km;
END //
DELIMITER ;