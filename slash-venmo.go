package main

import (
	"github.com/codegangsta/martini"
	"net/http"
)

func main() {
	m := martini.Classic()

	m.Get("/", func() string {
		return "Bot is healthy."
	})

	m.Post("/venmo", func(res http.ResponseWriter, req *http.Request) string {
		err := req.ParseForm()
		if err != nil {
			res.WriteHeader(500)
			return "Error parsing form"
		}

		//text := req.FormValue("text")

		return "Wootles"
	})

	m.Run()
}
