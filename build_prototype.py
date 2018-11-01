import urllib.request
import subprocess
import time

host = "10.0.0.14"
location = "/rc-build/"

def download_latest():
    url = "http://" + host + location + "latest_version.php"
    filename = urllib.request.urlopen(url).read().decode("utf-8")
    
    url = "http://" + host + location + filename
    urllib.request.urlretrieve(url, filename)
    
    print("Downloaded latest version")
    return filename

filename = download_latest()
current_time = time.time()
#, '-l', "log" + str(current_time) + ".txt"]
subprocess.call(['java', '-jar', filename, '-d'])

