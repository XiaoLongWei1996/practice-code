--key
local idKey = KEYS[1]
-- 过期时间
local timeout = 3;
if (redis.call("EXISTS", idKey) == 1) then
    -- 存在，加1
    redis.call("INCR", idKey)
    redis.call("expire", idKey, timeout)
else
    -- 不存在
    redis.call("set", idKey, 1)
    redis.call("expire", idKey, timeout)
end
local value = redis.call("get", idKey)
-- 补零
return string.format("%05d", value)