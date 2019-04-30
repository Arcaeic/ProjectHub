class Connection:
    source = ""
    destination = ""
    protocols_used = []

    # Fragments
    fragment_count = 0
    last_fragment_offset = 0

    # route:  {ttl: srcIP}
    route = {}

    # rtt: {srcIP: avg_time_list}
    rtt = {}

    def __init__(self, src_ip, dst_ip):
        self.source = src_ip
        self.destination = dst_ip
