const path = require("path");
module.exports = {
    mode: "development",
    resolve: {
        extensions: [".js", ".ts", ".tsx", ".png", "jpg"]
    },

    devServer: {
        historyApiFallback: true,
        port: 8083,
        proxy: {
            '/api': {
                target: {
                    host: "0.0.0.0",
                    protocol: 'http:',
                    port: 8080
                },
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    },
    devtool: "source-map",
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            },
            {
                test: /\.(jpg|jpeg|gif|png)$/,
                use: {
                    loader: 'file-loader',
                    options: {
                        name: '[name].[ext]',
                        publicPath: 'images',
                        outputPath: 'images',
                    }
                }
            },
            {
                test: /\.css$/i,
                use: ["style-loader", "css-loader"],
            },
        ]
    }
}