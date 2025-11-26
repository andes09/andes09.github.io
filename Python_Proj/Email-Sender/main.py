import smtplib

server = smtplib.SMTP('smtp.gmail.com', 587)
EMAIL = 'anshdesai2005@gmail.com'
PASSWORD = 'Andes@905'
server.starttls()
server.login(EMAIL, PASSWORD)

server.sendmail(EMAIL, 'anamikajain12@gmail.com', 'Hello, this is a test email')
print('Email sent successfully')
server.quit()