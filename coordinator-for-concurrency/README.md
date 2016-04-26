# oozie-investigate
## coordinator for concurrency

### coordinator中如果上一个时间点的workflow还没有结束，又一个workflow是推迟还是正常运行？--可以通过控制concurrency设置为1来实现workflow推迟到上一个时间点结束后再执行