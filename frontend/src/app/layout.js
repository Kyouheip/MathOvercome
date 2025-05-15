// /crs/app
import "./globals.css";

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
                    crossOrigin="anonymous"/>
      </head>

      <body className="bg-dark text-white">

        <header className="bg-secondary text-dark p-3 position-relative">
          <h1 className="mb-0">MathOvercome</h1>
          <div className="position-absolute bottom-0 end-0 me-5 mb-2 text-dark fs-5">
             ～数学の苦手を克服し大学受験を乗り越えよう！
          </div>
        </header>

        <main>{children}</main>
        
      </body>
    </html>
  );
}
