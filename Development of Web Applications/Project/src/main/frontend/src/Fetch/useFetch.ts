import {
    useState,
    useEffect,
} from 'react'

const baseUrl = "http://localhost:8083/api/"

export function useFetch(uri: string, options) : [content: any | undefined, loading: boolean] {
    const [loading, setLoading] = useState(false)
    const [content, setContent] = useState(undefined)
    const url = baseUrl + uri

    useEffect(() => {
        let cancelled = false
        async function doFetch() {

            const resp = await fetch(url,{
                method : (options.method == undefined ? "GET" : options.method),
                headers: options.headers,
                body: options.body
            })
            const body = await resp.json()
            if (!cancelled) {
                setLoading(false)
                setContent(body)
            }
        }
        setLoading(true)
        doFetch()
        return () => {
            cancelled = true
        }
    }, [url, setContent])

    return [content, loading]
}