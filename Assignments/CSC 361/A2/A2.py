import dpkt
import sys
import socket
from Connection import Connection

"""
@author: Jory Anderson
@date 2018-02-14 
(Yes, this is how I spent my Valentine's Day)

CSC 361 
Assignment 2
V00843894
Spring 2018
"""

RTTList = []
ackPairs = {}

connection_list = []
complete_connection_list = []

complete_connection_count = 0
reset_count = 0
open_count = 0

time_durations = []
packets_sent = []
packets_received = []
window_sizes = []

pcap_epoch = None


def traversePcap():
    global window_sizes

    for ts, buf in pcap:
        eth = dpkt.ethernet.Ethernet(buf)
        ip_header = eth.data
        tcp = ip_header.data
        time = ts - pcap_epoch

        updateRTT(tcp, time)
        updateConnectionList(ts, tcp, ip_header, len(buf))
        window_sizes.append(tcp.win)

    connectionCount()
    getStatistics()
    printDetails()


def updateRTT(tcp, time):
    global RTTList
    global ackPairs

    if tcp.flags & dpkt.tcp.TH_ACK != 0:
        if tcp.seq in ackPairs:
            RTTList.append(time - ackPairs.get(tcp.seq))
            del ackPairs[tcp.seq]
        else:
            pair = {tcp.ack: time}
            ackPairs.update(pair)


def updateConnectionList(ts, tcp, ip_header, packet_len):
    global connection_list

    src_ip = socket.inet_ntoa(ip_header.src)
    dst_ip = socket.inet_ntoa(ip_header.dst)

    curr_connection = (src_ip, tcp.sport, dst_ip, tcp.dport)
    inv_curr_connection = (dst_ip, tcp.dport, src_ip, tcp.sport)

    connection_found = connectionExists(curr_connection, inv_curr_connection)
    if connection_found is None:
        if len(connection_list) == 0:
            connection_list.append(Connection(0, 0, tcp, ip_header, packet_len))
        else:
            connection_list.append(Connection(ts, pcap_epoch, tcp, ip_header, packet_len))
    else:
        for connection in connection_list:

            # src -> dst
            if curr_connection == connection.connectionTuple:
                connection.updateFlags(tcp)
                connection.end = ts - pcap_epoch
                connection.srcPackets += 1
                connection.srcLen += packet_len

            # dst -> src
            elif inv_curr_connection == connection.connectionTuple:
                connection.updateFlags(tcp)
                connection.end = ts - pcap_epoch
                connection.dstPackets += 1
                connection.dstLen += packet_len


def connectionExists(curr_connection, inv_curr_connection):
    for connection in connection_list:
        if connection.connectionTuple == curr_connection or connection.connectionTuple == inv_curr_connection:
            return connection
    return None


def connectionCount():
    global complete_connection_count, open_count, reset_count
    global complete_connection_list

    for connection in connection_list:
        if connection.synCount > 0 and connection.finCount > 0:
            complete_connection_count += 1
            complete_connection_list.append(connection)
        if connection.resets > 0:
            reset_count += 1
        if connection.resets == 0 and connection.finCount == 0:
            open_count += 1


def getStatistics():
    global time_durations, packets_sent, packets_received

    for connection in complete_connection_list:
        time_durations.append(connection.end - connection.start)
        packets_sent.append(connection.srcPackets)
        packets_received.append(connection.dstPackets)


def printDetails():
    print("---General connection information---\n")
    print("Number of total connections: " + str(complete_connection_count + reset_count + open_count))
    print("Number of unique connections: " + str(len(connection_list)))
    print("Number of complete connections: " + str(complete_connection_count))
    print("Number of reset connections: " + str(reset_count))
    print("Number of open connections: " + str(open_count) + "\n")

    print("Duration:")
    printMinMaxAvgDuration(time_durations)

    print("Packet RTT: ")
    printMinMaxAvgDuration(RTTList)

    print("Packets sent:")
    printMinMaxAvg(packets_sent)

    print("Packets received:")
    printMinMaxAvg(packets_received)

    print("Window Sizes:")
    printMinMaxAvg(window_sizes)

    print("---Specific connection information---\n")
    i = 1
    for connection in connection_list:
        print("Connection " + str(i) + ":")
        connection.printConnectionInformation()
        i += 1


def printMinMaxAvg(li):
    print("\tMinimum: " + str(min(li)))
    print("\tAverage: " + str(int(sum(li)/len(li))))
    print("\tMaximum: " + str(max(li)) + "\n")


def printMinMaxAvgDuration(li):
    print("\tMinimum: " + str(min(li)))
    print("\tAverage: " + str(sum(li)/len(li)))
    print("\tMaximum: " + str(max(li)) + " (seconds)" + "\n")


def openTraceFile():
    global pcap_epoch

    f = open(sys.argv[1], 'rb')
    pcap_reader = dpkt.pcap.Reader(f)
    for ts, buf in pcap_reader:
        pcap_epoch = ts
        break

    return pcap_reader


if __name__ == '__main__':
    global pcap

    if len(sys.argv) != 2:
        raise Exception("No capture file provided.")
    pcap = openTraceFile()
    traversePcap()
