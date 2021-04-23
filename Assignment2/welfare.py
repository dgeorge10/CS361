#!/usr/bin/env python3

a = [2,4,5,6,7,8]
b = [1,2,3,4,7]
c = [3,5,7,9]

i = 0
j = 0
k = 0

while True:
    if a[i] < b[j] or a[i] < c[k]:
        i += 1
    elif b[j] < a[i] or b[j] < c[k]:
        j += 1
    elif c[k] < b[j] or c[k] < a[i]:
        k += 1
    else:
        break

print(i,j,k)
