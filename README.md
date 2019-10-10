# teagile-mail-service
Mail Service for TEAgile project

Install RabbitMQ: https://www.rabbitmq.com/install-windows.html#installer
				  https://www.youtube.com/watch?v=oRIF1xKEI0I
				  
To run the rabbitmq server: 

- cd C:\Program Files\RabbitMQ Server\rabbitmq_server-3.8.0
- cd sbin
- rabbitmq-service.bat start / rabbitmq-service.bat stop
- rabbitmqctl status

To enable rabbitmq interface:

- cd C:\Program Files\RabbitMQ Server\rabbitmq_server-3.8.0
- cd sbin 
- rabbitmq-plugins enable rabbitmq_management
- go to: localhost:15672
- login with: 
	user: guest
	password: guest