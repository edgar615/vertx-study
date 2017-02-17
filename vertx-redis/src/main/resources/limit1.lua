FUNCTION LIMIT_API_CALL(ip)
  ts = CURRENT_UNIX_TIME()
  keyname = ip+":"+ts
  current = GET(keyname)
  IF current != NULL AND current > 10 THEN
      ERROR "too many requests per second"
  ELSE
      MULTI
          INCR(keyname,1)
          EXPIRE(keyname,10)
      EXEC
      PERFORM_API_CALL()
  END
END