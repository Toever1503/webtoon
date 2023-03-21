import axios from "axios";

let axiosInstance: any = null;

function getInstance() {
    if (axiosInstance != null) {
        return axiosInstance
    }
    axiosInstance = axios.create({
        // baseURL: "http://localhost:8080",
        headers: {},
    });

    //hook interceptor cài ở đây
    axiosInstance.interceptors.request.use((config: { headers: { Authorization: string; }; }) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    })

    axiosInstance.interceptors.response.use((response: any) => {
        return response;
    },
        async (error: { response: { data: { code: string; }; }; }) => {
            // if (error.response.status === 401) {
            //     localStorage.removeItem('token');
            //     // window.location.href = '/login';
            // }

            if (error.response.data)
                if (error.response.data.code === "code-4") {
                    window.location.href =
                        "/login?redirectTo=" + window.location.pathname;
                }
            return Promise.reject(error);
        }
    );
    return axiosInstance;
}

function get(endpointApiUrl: any, payload = {}) {
    return getInstance().get(endpointApiUrl, {
        params: payload,
    });
}

function post(endpointApiUrl: any, payload = {}) {
    return getInstance().post(endpointApiUrl, payload);
}

function put(endpointApiUrl: any, payload = {}) {
    return getInstance().put(endpointApiUrl, payload);
}

function del(endpointApiUrl: any, payload = {}) {
    return getInstance().delete(endpointApiUrl, { data: payload });
}

function de(endpointApiUrl: any, payload = {}) {
    return getInstance().delete(endpointApiUrl, { data: payload });
}

export const accountAxios = {
    axiosInstance,
    get,
    post,
    put,
    del,
    de,
};
