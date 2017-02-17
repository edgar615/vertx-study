local key = "rate.limit:" .. KEYS[1] --限流KEY
local limit = tonumber(ARGV[1]) --限流大小
local expire_time = ARGV[2] --过期时间
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --超出限流大小
  return 0
else --请求数+1，并设置expire_time秒之后过期
  redis.call("INCRBY", key, "1")
  redis.call("EXPIRE", key, expire_time)
  return 1
end