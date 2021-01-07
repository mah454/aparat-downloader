## Aparat PlayList Downloader
#### Build and run : 
`mvn clean compile package`  


#### Install on linux os : 
only execute install.sh script as root user   
`root@HOSTNAME:~# install.sh` 

#### Usage :      
`aparat-dl [APARAT_URL_PATH] [QUALITY-ID : 144|240|360|480|720|BEST]`     
**Note:** default quality is `BEST`      
##### Example :  
`aparat-dl https://www.aparat.com/playlist/108222`    
`aparat-dl https://www.aparat.com/playlist/108222 BEST`    
`aparat-dl https://www.aparat.com/playlist/108222 480`   