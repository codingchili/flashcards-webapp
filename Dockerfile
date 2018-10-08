FROM anapsix/alpine-java:latest

MAINTAINER codingchili@github

# make sure to run 'gradlew archiveZip' before building the container.
# example: 'gradlew archiveZip && docker build -f Dockerfile build/distributions'

RUN mkdir -p /opt/flash/
COPY ./ /opt/flash/

RUN cd /opt/flash && \
    unzip ./* && \
    rm *.zip

# for the webserver
EXPOSE 443/tcp
# for the API
EXPOSE 8180/tcp

ENTRYPOINT ["/bin/sh", "-c", "cd /opt/flash && ./run.sh"]
