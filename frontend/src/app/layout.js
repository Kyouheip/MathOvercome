// /crs/app
//import "./globals.css";

export const metadata = {
  title: "MathOvercome"
};

export default function RootLayout({ children }) {
  return (
    <html lang="ja">
      <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                    rel="stylesheet"
                    integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
                    crossorigin="anonymous"/>
      </head>

      <body>

        <header>
        <h1>MathOvercome</h1>
        </header>

        <main>{children}</main>
        
      </body>
    </html>
  );
}
