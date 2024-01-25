--秒杀就是使用redis中的set类型判断用户是否下单，string类型保存票的库存
--秒杀主要依托于redis的线程安全，利用LUA脚本的原子性保证库存和下单之间的数据一致性，

--票库存key
local ticketKey = KEYS[1]
--票id
local ticketId = ARGV[1]
--用户下单key
local userKey = KEYS[2]
--用户id
local userId = ARGV[2]

--1.判断票的库存是否足够
if (tonumber(redis.call("get", ticketKey)) < 1) then
    --库存不足
    return 1
end
--2.判断用户是否下单
if (redis.call("sismember", userKey, userId) == 1) then
    --用户已下单
    return 2
end
--3.用户未下单，可下单，添加下单记录
redis.call("sadd", userKey, userId)
--4.扣减库存
redis.call("decr", ticketKey)
--抢票成功
return 0