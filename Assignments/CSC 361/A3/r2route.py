import sys
import dpkt
import socket
from Connection import Connection

windows_trace = False
linux_trace = False
mainConnection = None
pcap = None
pcap_epoch = 0


def traverse_windows():
    global mainConnection

    trace_started = False
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

        if mainConnection is not None:
            # Grab protocols
            curr_protocol = ip.p
            if curr_protocol not in mainConnection.protocols_used:
                mainConnection.protocols_used.append(curr_protocol)

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
        elif icmp.type is 11:
            internal_ip = icmp.data.data
            internal_icmp = internal_ip.data
            src_ip = socket.inet_ntoa(ip.src)
            reply_seq = internal_icmp.data.seq

        # If old seg equals new seg and ttl not in map, Map {ttl, IP}
        if reply_seq in seq_ttl_map.keys() and seq_ttl_map[reply_seq] not in mainConnection.route:
            mainConnection.route.update({seq_ttl_map[reply_seq]: src_ip})
            seq_ttl_map.pop(reply_seq)

    print_details()


def traverse_linux():
    global mainConnection
    global id_offset_map, id_count_map

    trace_started = False
    reply_port_pair = (-1, -1)
    src_ip = ""

    ports_ttl_map = {}

    for ts, buf in pcap:

        # Grab ethernet frame
        eth = dpkt.ethernet.Ethernet(buf)

        # Check if frame contains an IP packet/header
        if not isinstance(eth.data, dpkt.ip.IP):
            continue

        # Grab IP packet
        ip = eth.data

        if mainConnection is not None:
            # Grab protocols
            curr_protocol = ip.p
            if curr_protocol not in mainConnection.protocols_used:
                mainConnection.protocols_used.append(curr_protocol)

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
                src_ip = socket.inet_ntoa(ip.src)
                dst_ip = socket.inet_ntoa(ip.dst)
                mainConnection = Connection(src_ip, dst_ip)
                trace_started = True

        # Get Reply Information
        elif isinstance(ip.data, dpkt.icmp.ICMP) and ip.data.type is 11:
            icmp = ip.data
            internal_ip = icmp.data.data
            udp = internal_ip.data
            src_ip = socket.inet_ntoa(ip.src)
            reply_port_pair = (udp.sport, udp.dport)

        if reply_port_pair in ports_ttl_map.keys() and ports_ttl_map[reply_port_pair] not in mainConnection.route:
            mainConnection.route.update({ports_ttl_map[reply_port_pair]: src_ip})
            ports_ttl_map.pop(reply_port_pair)

    # TODO: Get RTT
    print_details()


def print_details():
    if mainConnection is None:
        raise Exception("mainConnection is Null!")

    print("The IP address of the source node: " + mainConnection.source)
    print("The IP address of ultimate destination node: " + mainConnection.destination)
    print("The IP addresses of the intermediate destination nodes:")
    for key in sorted(mainConnection.route.keys()):
        print("\trouter " + str(key) + ": " + mainConnection.route[key], )


def get_trace_type():
    global windows_trace
    global linux_trace
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
        print("Usage: <program> [PLACEHOLDER!] <pcap>")
        raise Exception("No capture file provided, or too many arguments provided")

    pcap = open_tracefile()
    if get_trace_type() is "Windows":
        traverse_windows()
    else:
        traverse_linux()