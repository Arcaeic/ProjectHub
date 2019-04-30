import sys
from math import trunc
from statistics import *
import dpkt
import socket
from Connection import Connection

trace_started = False
mainConnection = None
pcap = None
pcap_epoch = 0
protocol_map = {1: "ICMP",
                6: "TCP",
                17: "UDP",
                47: "GRE",
                51: "AH",
                50: "ESP",
                8: "EGP",
                3: "GGP",
                20: "HMP",
                88: "IGMP",
                66: "RVD",
                89: "OSPF",
                12: "PUP",
                27: "RDP",
                46: "RSVP QoS"}
id_offset_map = {}
id_count_map = {}
id_time_map = {}


def traverse_windows():
    global mainConnection, trace_started

    reply_seq = 0
    src_ip = ""
    seq_ttl_map = {}

    for ts, buf in pcap:
        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)

        # Check if frame contains an IP packet/header
        if not isinstance(eth.data, dpkt.ip.IP):
            continue

        # Grab IP packet
        ip = eth.data

        update_fragments(ip, ts)
        update_protocols(ip)

        # Make sure we are dealing with an ICMP
        if not isinstance(ip.data, dpkt.icmp.ICMP):
            continue

        icmp = ip.data

        if icmp.type is 8:
            request_seq = icmp.data.seq
            curr_ttl = ip.ttl
            seq_ttl_map.update({request_seq: curr_ttl})
            if curr_ttl is 1 and not trace_started:
                src_ip = socket.inet_ntoa(ip.src)
                dst_ip = socket.inet_ntoa(ip.dst)
                mainConnection = Connection(src_ip, dst_ip)
                trace_started = True

        # If type 11 (TTL exceeded), grab source IP, ttl, and the internal seg.
        elif icmp.type is 11 or icmp.type is 3:
            internal_ip = icmp.data.data
            internal_icmp = internal_ip.data
            src_ip = socket.inet_ntoa(ip.src)
            reply_seq = internal_icmp.data.seq

        # If old seg equals new seg and ttl not in map, Map {ttl, IP}
        if reply_seq in seq_ttl_map.keys() and seq_ttl_map[reply_seq] not in mainConnection.route:
            mainConnection.route.update({seq_ttl_map[reply_seq]: src_ip})
    get_rtt_windows(seq_ttl_map)


def get_rtt_windows(seq_ttl_map):
    global mainConnection

    seq_time_map = {}

    f = open(sys.argv[1], 'rb')
    pcap = dpkt.pcapng.Reader(f)
    f.flush()
    for ts, buf in pcap:
        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)
        # Check if frame contains an IP packet/header
        if not isinstance(eth.data, dpkt.ip.IP):
            continue

        # Grab IP packet
        ip = eth.data

        # Make sure we are dealing with an ICMP
        if not isinstance(ip.data, dpkt.icmp.ICMP):
            continue

        icmp = ip.data

        if icmp.type is 8:
            request_seq = icmp.data.seq
            if request_seq in seq_ttl_map:
                request_time = ts - pcap_epoch
                seq_time_map.update({request_seq: request_time})

        # If type 11 (TTL exceeded), grab source IP, ttl, and the internal seg.
        elif icmp.type is 11 or icmp.type is 3:
            internal_ip = icmp.data.data
            internal_icmp = internal_ip.data
            src_ip = socket.inet_ntoa(ip.src)
            reply_seq = internal_icmp.data.seq
            reply_time = ts - pcap_epoch

            if reply_seq in seq_time_map.keys() and src_ip in mainConnection.route.values():
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})

                mainConnection.rtt[src_ip].append(reply_time - seq_time_map[reply_seq])
            if ip.id in id_time_map.keys():
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})
                id_time_map[ip.id] = [reply_time - x for x in id_time_map[ip.id]]
                mainConnection.rtt[src_ip].extend(id_time_map[ip.id])

        elif icmp.type is 0:
            src_ip = socket.inet_ntoa(ip.src)
            reply_seq = icmp.data.seq

            if reply_seq in seq_time_map.keys() and src_ip == mainConnection.destination:
                reply_time = ts - pcap_epoch
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})

                mainConnection.rtt[src_ip].append(reply_time - seq_time_map[reply_seq])


def traverse_linux():
    global mainConnection, trace_started
    global id_offset_map, id_count_map

    reply_port_pair = (-1, -1)
    ports_ttl_map = {}

    for ts, buf in pcap:
        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)

        # Check if frame contains an IP packet/header
        if not isinstance(eth.data, dpkt.ip.IP):
            continue

        # Grab IP packet
        ip = eth.data
        src_ip = socket.inet_ntoa(ip.src)

        update_fragments(ip, ts)
        update_protocols(ip)

        # Make sure we are dealing with an UDP datagram
        if not (isinstance(ip.data, dpkt.udp.UDP) or isinstance(ip.data, dpkt.icmp.ICMP)):
            continue
        # Get Request Information
        if isinstance(ip.data, dpkt.udp.UDP):
            udp = ip.data
            request_port_pair = (udp.sport, udp.dport)
            curr_ttl = ip.ttl
            ports_ttl_map.update({request_port_pair: curr_ttl})
            if curr_ttl is 1 and not trace_started:
                dst_ip = socket.inet_ntoa(ip.dst)
                mainConnection = Connection(src_ip, dst_ip)
                trace_started = True

        # Get Reply Information
        elif isinstance(ip.data, dpkt.icmp.ICMP) and (ip.data.type is 11 or ip.data.type is 3):
            icmp = ip.data
            internal_ip = icmp.data.data
            udp = internal_ip.data
            reply_port_pair = (udp.sport, udp.dport)

        if reply_port_pair in ports_ttl_map.keys() and ports_ttl_map[reply_port_pair] not in mainConnection.route:
            mainConnection.route.update({ports_ttl_map[reply_port_pair]: src_ip})

    get_rtt_linux(ports_ttl_map)


def get_rtt_linux(ports_ttl_map):
    global mainConnection

    ports_time_map = {}
    ports_id_map = {}

    f = open(sys.argv[1], 'rb')
    pcap = dpkt.pcapng.Reader(f)
    f.flush()

    for ts, buf in pcap:

        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)

        # Check if frame contains an IP packet/header
        if not isinstance(eth.data, dpkt.ip.IP):
            continue

        # Grab IP packet
        ip = eth.data
        src_ip = socket.inet_ntoa(ip.src)

        # Make sure we are dealing with an UDP datagram
        if not (isinstance(ip.data, dpkt.udp.UDP) or isinstance(ip.data, dpkt.icmp.ICMP)):
            continue

        # Get Request Information
        if isinstance(ip.data, dpkt.udp.UDP):
            udp = ip.data
            request_port_pair = (udp.sport, udp.dport)
            if request_port_pair[::-1] in ports_time_map.keys() and src_ip == mainConnection.destination:
                reply_time = ts - pcap_epoch
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})

                mainConnection.rtt[src_ip].append(reply_time - ports_time_map[request_port_pair[::-1]])
            if request_port_pair in ports_ttl_map:
                request_time = ts - pcap_epoch
                ports_time_map.update({request_port_pair: request_time})
                ports_id_map.update({request_port_pair: ip.id})

        # Get Reply Information
        elif isinstance(ip.data, dpkt.icmp.ICMP) and (ip.data.type is 11 or ip.data.type is 3):
            icmp = ip.data
            internal_ip = icmp.data.data
            udp = internal_ip.data
            reply_port_pair = (udp.sport, udp.dport)
            reply_time = ts - pcap_epoch

            if reply_port_pair in ports_time_map.keys() and src_ip in mainConnection.route.values():
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})

                mainConnection.rtt[src_ip].append(reply_time - ports_time_map[reply_port_pair])
            if ip.id in id_time_map.keys():
                if src_ip not in mainConnection.rtt.keys():
                    mainConnection.rtt.update({src_ip: []})
                id_time_map[ip.id] = [reply_time - x for x in id_time_map[ip.id]]
                mainConnection.rtt[src_ip].extend(id_time_map[ip.id])


def update_protocols(ip):
    if mainConnection is not None:
        # Grab protocols
        curr_protocol = ip.p
        if curr_protocol not in mainConnection.protocols_used:
            mainConnection.protocols_used.append(curr_protocol)


def update_fragments(ip, ts):
    global id_time_map, id_offset_map, id_count_map
    # If id is not in either map, initialize
    # Update frag_count map, and offset_map
    if ip.off & dpkt.ip.IP_MF or ip.id in id_count_map:
        # Update fragment info
        if ip.id not in id_count_map and ip.id not in id_offset_map:
            id_count_map.update({ip.id: 1})
            id_offset_map.update({ip.id: ip.offset})
        else:
            id_count_map[ip.id] += 1
        # Get offset
        id_offset_map[ip.id] = max(ip.offset, id_offset_map[ip.id])
        if ip.id not in id_time_map.keys():
            id_time_map.update({ip.id: []})
        id_time_map[ip.id].append(ts - pcap_epoch)


def print_details():

    if mainConnection is None:
        raise Exception("mainConnection is Null!")

    print("The IP address of the source node: " + mainConnection.source)
    print("\nThe IP address of ultimate destination node: " + mainConnection.destination)
    print("\nThe IP addresses of the intermediate destination nodes:")
    for key in sorted(mainConnection.route.keys()):
        if mainConnection.route[key] == mainConnection.destination:
            continue
        print("\trouter " + str(key) + ": " + mainConnection.route[key], )
    print("\nThe values in the protocol field of IP headers:")

    for value in sorted(mainConnection.protocols_used):
        if value in protocol_map:
            print("\t" + str(value) + ": " + protocol_map.get(value))
        else:
            print("\t" + str(value))

    # Fragments
    if len(id_count_map) > 0:
        for id in id_count_map:
            print("\nThe number of fragments created from the datagram " + str(id) + " is: " + str(id_count_map[id]))
            print("The offset of the last fragment was: " + str(id_offset_map[id]))
    else:
        print("\nThe number of fragments created from the original datagram is: 0")
        print("The offset of the last fragment is: 0")
    print()

    # RTT
    for intermed_ip in mainConnection.route.values():
        if intermed_ip in mainConnection.rtt.keys():
            if intermed_ip == mainConnection.destination:
                continue
            std = 0
            rtt = str(
                trunc(mean(mainConnection.rtt[intermed_ip]) * 1000))
            if len(mainConnection.rtt[intermed_ip]) > 1:
                std = trunc(stdev(mainConnection.rtt[intermed_ip]) * 1000)
            print("The avg RTT between " + mainConnection.source + " and " + intermed_ip + " is: "
                  + rtt + " ms" + ", the s.d is: " + str(std) + " ms")
    std = 0
    rtt = trunc(mean(mainConnection.rtt[mainConnection.destination]) * 1000)
    if len(mainConnection.rtt[mainConnection.destination]) > 1:
        std = trunc(stdev(mainConnection.rtt[mainConnection.destination]) * 1000)
    print("The avg RTT between " + mainConnection.source + " and " + mainConnection.destination + " is: "
          + str(rtt) + " ms" + ", the s.d is: " + str(std) + " ms")


def get_trace_type():
    for ts, buf in pcap:

        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)

        # Check if frame contains an IPV4 packet/header
        if not isinstance(eth.data, dpkt.ip.IP) or isinstance(eth.data, dpkt.ip6.IP6):
            continue

        # Grab IP packet
        ip = eth.data

        if not isinstance(ip.data, dpkt.icmp.ICMP) and not isinstance(ip.data, dpkt.udp.UDP):
            continue

        if isinstance(ip.data, dpkt.icmp.ICMP):
            return "Windows"
        else:
            return "Linux"


def open_tracefile():
    global pcap_epoch

    f = open(sys.argv[1], 'rb')
    pcap_reader = dpkt.pcapng.Reader(f)
    for ts, buf in pcap_reader:
        pcap_epoch = ts
        break

    return pcap_reader


if __name__ == '__main__':

    if len(sys.argv) != 2:
        print("Usage: python3 trace.py <path to pcap>")
        raise Exception("No capture file provided, or too many arguments provided")

    pcap = open_tracefile()
    if get_trace_type() is "Windows":
        traverse_windows()
    else:
        traverse_linux()

    print_details()
