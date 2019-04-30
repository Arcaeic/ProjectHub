# thread pro con
Created Thursday 08 March 2018

queue fifoqueue = new queue
thread(producer)
thread(consumer)
producer.join
consumer.join

producer {
while items to insert
fifoqueue.insert(item)
}
	
}

consumer {
whi;e items to consume
item = fifoqueue.pop()
...
}


