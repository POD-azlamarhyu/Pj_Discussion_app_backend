FROM eclipse-temurin:21.0.7_6-jdk
ARG API_PORT
ARG LOCALDEV_WORKDIR
ARG JAVA_CONTAINER_USER
ARG JAVA_CONTAINER_USER_ID
ARG APP_NAME
ARG BUILD_WORKDIR

RUN if ! getent group ${JAVA_CONTAINER_USER_ID} > /dev/null 2>&1; then \
        groupadd -g ${JAVA_CONTAINER_USER_ID} ${JAVA_CONTAINER_USER}; \
    fi && \
    if ! id -u ${JAVA_CONTAINER_USER_ID} > /dev/null 2>&1; then \ 
        useradd -u ${JAVA_CONTAINER_USER_ID} -g ${JAVA_CONTAINER_USER} -m ${JAVA_CONTAINER_USER}; \
    fi && \
    useradd -g ${JAVA_CONTAINER_USER_ID} -m ${JAVA_CONTAINER_USER}

WORKDIR ${LOCALDEV_WORKDIR}

COPY --chown=${JAVA_CONTAINER_USER_ID}:${JAVA_CONTAINER_USER_ID} gradlew build.gradle settings.gradle ./
COPY --chown=${JAVA_CONTAINER_USER_ID}:${JAVA_CONTAINER_USER_ID} gradle ./gradle

USER ${JAVA_CONTAINER_USER_ID}

RUN ./gradlew dependencies --no-daemon || true

EXPOSE ${API_PORT}
