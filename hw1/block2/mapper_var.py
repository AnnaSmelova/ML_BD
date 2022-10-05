#!/usr/bin/python3
import sys
import csv


CHUNK_SIZE_DEFAULT = 100


def get_chunk_stats(chunk):
    size = len(chunk)
    mean = sum(chunk) / size
    var = sum((x - mean)**2 for x in chunk) / size
    print(size, var, mean)


def main():
    current_chunk_size = 0
    current_chunk = []

    price_data = csv.reader(sys.stdin)
    for price in price_data:
        try:
            current_price = float(price[0].rstrip())
        except:
            continue
        current_chunk.append(current_price)
        current_chunk_size += 1
        if current_chunk_size == CHUNK_SIZE_DEFAULT:
            get_chunk_stats(current_chunk)
            current_chunk.clear()
            current_chunk_size = 0
    if current_chunk_size:
        get_chunk_stats(current_chunk)


if __name__ == "__main__":
    main()
