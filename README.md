# Register example
This demo application show how to containerize one simple Servelet applicaiton and deploy it into ESDP.

Register example use Tomcat as container, Redis as memory cache and Galera(Mysql cluster) as persistent storage.
So it also demonstrates how the Tomcat, Redis and Mysql leverage ESDP to achieve high availability.


Register is a Servelet application which can be held in j2ee container.
When it's running, user can:
* Access "${Host}/reg/reg" to add a new user item.
* Access "${Host}/reg/search?name=${Name}" to search if one user already exist with user name.

# Build register image
Go into the root of the application source:

```
# maven package
# docker build . -t register
```
> If you don't want to buid image yourself, you can change "image: register " to "image: wodeamd/register " inside reg-template.yaml

# Create a new ESDP project
Redis and Mysql require root privilege, so here we also need to do scc setting. 

Create project "register" from ESDP ui.
```

# oc project register
# oadm policy add-scc-to-group anyuid system:register:authenticated
```

# Deploy application
Process template form ESDP ui or use command as follow.

```
# oc new-app -f reg-template.yaml
```

> If you find accessing "gcr.io/google_containers" stuck. change the reference to "wodeamd"
