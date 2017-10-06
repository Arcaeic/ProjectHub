#!/usr/bin/env python3

import sys
import argparse
from textformatter import Formatter

def main():
    input = [ \
        ".LW 30",
        "Properly formatting  a   file where",
        "there  ",
        "is",
        "    a smattering of white space throughout",
        "  really means eliminating that  ",
        "extra",
        "   white ",
        "      space",
        "such that the result",
        "    looks neat    ",
        "                   and",
        "                         very",
        "            tidy."
    ]


    # Instantiate a Formatter object with a list of strings to be
    # formatted.

    f = Formatter(None, input)
    lines = f.get_lines()

    # There was a typo here in the original file. Oops. Now it is fixed.
    for l in lines:
        print (l)


if __name__ == "__main__":
    main()
