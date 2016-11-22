import csv
import random

with open('names.csv', 'rb') as f:
    reader = csv.DictReader(f, ['FirstName', 'FamilyName'])
    header_flag = False
    with open('sample-data.csv', 'wb') as output:
        writer = csv.DictWriter(output, ['FirstName', 'FamilyName', 'Phone', 'GroupId'], delimiter=',', quoting=csv.QUOTE_MINIMAL)
        writer.writeheader()
        for row in reader:
            if not header_flag:
                header_flag = True
                continue
            record = row
            record['Phone'] = str(random.randrange(10000, 99999))
            record['GroupId'] = str(random.randrange(1, 8))
            writer.writerow(record)
