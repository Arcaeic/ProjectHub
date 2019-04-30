import dpkt
import socket


class Connection:

    connectionTuple = None  # (srcIP, srcPort, dstIP, dstPort)
    synCount = 0
    finCount = 0
    start = None    # measured in seconds
    end = None  # measured in seconds
    srcPackets = 0  # (sent packets)
    srcLen = 0  # measured in bytes
    dstLen = 0  # measured in bytes
    dstPackets = 0  # (received packets)
    resets = 0  # (number of)

    #   updateFlags()
    #   checks for any relevant flags, and updates the connection.
    def updateFlags(self, tcp):
        if (tcp.flags & dpkt.tcp.TH_SYN) != 0:
            self.synCount += 1
        elif (tcp.flags & dpkt.tcp.TH_FIN) != 0:
            self.finCount += 1
        elif (tcp.flags & dpkt.tcp.TH_RST) != 0:
            self.resets += 1

    #   printConnectionInformation()
    #   outputs the connection-specific information
    def printConnectionInformation(self):
        print("Source Address: " + str(self.connectionTuple[0]))
        print("Source Port: " + str(self.connectionTuple[1]))
        print("Destination Address: " + str(self.connectionTuple[2]))
        print("Destination Port: " + str(self.connectionTuple[3]))
        print("Status: S" + str(self.synCount) + "F" + str(self.finCount))
        if self.synCount == 0 or self.finCount == 0:
            print()
        else:
            print("Start: " + str(self.start) + "\tEnd: " + str(self.end) + " (seconds)")
            print("Duration: " + str(self.end - self.start) + " (seconds)")
            print("Packets sent: " + str(self.srcPackets) +
                  "\t Packets Received: " + str(self.dstPackets))
            print("Data sent in bytes: " + str(self.srcLen))
            print("Data received in bytes: " + str(self.dstLen))
            print("Number of resets: " + str(self.resets) + "\n")

    def __init__(self, ts, pcap_epoch, tcp, ip_header, packet_length):
        src_ip = socket.inet_ntoa(ip_header.src)
        dst_ip = socket.inet_ntoa(ip_header.dst)

        self.connectionTuple = (src_ip, tcp.sport,
                                dst_ip, tcp.dport)
        self.updateFlags(tcp)
        self.start = ts - pcap_epoch
        self.srcPackets += 1
        self.srcLen = packet_length
