import re

with open(r'C:\Users\aaa\workspace\cosmic\ecg_intelligence\src\main\java\com\hnkjzy\ecg\mapper\ApiSpecMapper.java', 'r', encoding='utf-8') as f:
    text = f.read()

text = text.replace("CONCAT(DATEDIFF(CURDATE(), p.inpatient_date), 'Ìì') AS day,", 
                r"CONCAT(IFNULL(DATEDIFF(CURDATE(), COALESCE(p.inpatient_date, p.create_time)), 0), 'Ìì') AS day,")

text = text.replace("DATEDIFF(CURDATE(), p.inpatient_date) AS hospital_days,", 
                r"IFNULL(DATEDIFF(CURDATE(), COALESCE(p.inpatient_date, p.create_time)), 0) AS hospital_days,")

with open(r'C:\Users\aaa\workspace\cosmic\ecg_intelligence\src\main\java\com\hnkjzy\ecg\mapper\ApiSpecMapper.java', 'w', encoding='utf-8') as f:
    f.write(text)
