FROM python27

ONBUILD COPY requirements.txt /opt/app/
ONBUILD RUN pip install --no-cache-dir -r requirements.txt

ONBUILD COPY . /opt/app

#To build: docker build --tag python27-onbuild .
