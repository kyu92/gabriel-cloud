const CleanWebpackPlugin = require("clean-webpack-plugin").CleanWebpackPlugin,
    path = require("path");

module.exports = {
    publicPath: "/",
    runtimeCompiler: true,
    lintOnSave: false,
    // outputDir: path.resolve(__dirname, "dist"),
    devServer: {
        port: 8000,
        hot: true,
        open: false,
        disableHostCheck: true
    },
    configureWebpack: {
        output: {
            filename: "[name].[hash:6].js",
            path: path.resolve(__dirname, "dist"),
        },
        plugins: [
            new CleanWebpackPlugin(),
        ],
    },
    chainWebpack: config => {
        config.module
            .rule("iview")
            .test(/iview.src.*?js$/)
            .use("babel")
            .loader("babel-loader")
            .end();
        config.plugin("html")
            .tap(args => {
                args[0].title = "Gabriel Web阅读器";
                return args;
            })
            .end();
    }
}