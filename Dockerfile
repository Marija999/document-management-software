FROM logicaldoc/logicaldoc

MAINTAINER LogicalDOC <packagers@logicaldoc.com>

# set default variables for LogicalDOC install
ENV LDOC_VERSION="8.7.4"
ENV LDOC_MEMORY="3000"
ENV LDOC_USERNO=""
ENV DEBIAN_FRONTEND="noninteractive"
ENV DB_ENGINE="mysql"
ENV DB_HOST="mysql-ld"
ENV DB_PORT="3306"
ENV DB_NAME="logicaldoc"
ENV DB_INSTANCE=""
ENV DB_USER="ldoc"
ENV DB_PASSWORD="changeme"
ENV DB_MANUALURL="false"
ENV DB_URL=""


RUN mkdir -p home/LogicalDOC
COPY . home/LogicalDOC


# Install the Tesseract OCR
RUN apt update
RUN apt-get -y install tesseract-ocr tesseract-ocr-deu tesseract-ocr-fra tesseract-ocr-spa tesseract-ocr-ita

# prepare system for java installation (to be removed)
RUN apt-get update && \
  apt-get -y install software-properties-common

# Packages needed to install LogicalDOC Enterprise
RUN apt-get -y install \
    curl \    
    unzip \    
    imagemagick \
    ghostscript \
    python3-jinja2 \
    python3-pip \
    mariadb-client \
    postgresql-client \
    vim \
    nano \
    sed \
    zip \
    wget \
    openssl \
    ftp \
    clamav \
    libfreetype6 \
    libreoffice \
    apt-utils \
    wget

# Download and unzip LogicalDOC installer 
#lll565
RUN echo 'y' | curl -L https://s3.amazonaws.com/logicaldoc-dist/logicaldoc/installers/logicaldoc-installer-${LDOC_VERSION}.zip \
    -o /LogicalDOC/logicaldoc-installer-${LDOC_VERSION}.zip && \
    unzip /LogicalDOC/logicaldoc-installer-${LDOC_VERSION}.zip -d /LogicalDOC && \
    rm /LogicalDOC/logicaldoc-installer-${LDOC_VERSION}.zip

# Fix the security policies of ImageMagick
RUN sed -i 's/<\/policymap>/  <policy domain=\"coder\" rights=\"read|write\" pattern=\"PDF\" \/><\/policymap>/' /etc/ImageMagick-6/policy.xml

# Install j2cli for the transformation of the templates (Jinja2)
RUN pip install j2cli

COPY . .
# Volumes for persistent storage
VOLUME /LogicalDOC/conf
VOLUME /LogicalDOC/repository

EXPOSE 8080

CMD ["/LogicalDOC/logicaldoc.sh", "run"]
