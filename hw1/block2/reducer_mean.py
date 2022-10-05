#!/usr/bin/python3
import sys


def main():
    current_size = 0
    current_mean = None

    for line in sys.stdin:
        chunk_size, chunk_mean = map(float, line.split())

        if not current_size:
            current_size, current_mean = chunk_size, chunk_mean
            continue

        current_mean = current_size * current_mean + chunk_size * chunk_mean
        current_mean /= current_size + chunk_size

        current_size += chunk_size

    print(current_mean)


if __name__ == "__main__":
    main()
