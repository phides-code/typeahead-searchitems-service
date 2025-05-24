package main

type Entity struct {
	Id        string `json:"id" dynamodbav:"id"`
	Content   string `json:"content" dynamodbav:"content"`
	CreatedOn uint64 `json:"createdOn" dynamodbav:"createdOn"`

	LowerCaseContent string `json:"lowerCaseContent" dynamodbav:"lowerCaseContent"`
}

type NewEntity struct {
	Content string `json:"content" validate:"required"`
}

type UpdatedEntity struct {
	Content string `json:"content" validate:"required"`
}

type ResponseEntity struct {
	Id        string `json:"id"`
	Content   string `json:"content"`
	CreatedOn uint64 `json:"createdOn"`
}
