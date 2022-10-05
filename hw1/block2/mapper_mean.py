#!/usr/bin/python3
import sys
import csv


CHUNK_SIZE_DEFAULT = 100


def get_chunk_mean(chunk):
    size = len(chunk)
    mean = sum(chunk) / size
    print(size, mean)


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
            get_chunk_mean(current_chunk)
            current_chunk.clear()
            current_chunk_size = 0
    if current_chunk_size:
        get_chunk_mean(current_chunk)


if __name__ == "__main__":
    main()
